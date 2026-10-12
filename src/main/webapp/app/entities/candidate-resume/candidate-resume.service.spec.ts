import { beforeEach, describe, expect, it, vi } from 'vitest';

import axios from 'axios';
import dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from '@/shared/composables/date-format';
import { CandidateResume } from '@/shared/model/candidate-resume.model';

import CandidateResumeService from './candidate-resume.service';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: vi.spyOn(axios, 'get'),
  post: vi.spyOn(axios, 'post'),
  put: vi.spyOn(axios, 'put'),
  patch: vi.spyOn(axios, 'patch'),
  delete: vi.spyOn(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('CandidateResume Service', () => {
    let service: CandidateResumeService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new CandidateResumeService();
      currentDate = new Date();
      elemDefault = new CandidateResume(123, 0, currentDate, 'AAAAAAA', 'AAAAAAA', 0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', false, 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = { receivedAt: dayjs(currentDate).format(DATE_TIME_FORMAT), ...elemDefault };
        axiosStub.get.mockResolvedValue({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.mockRejectedValue(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a CandidateResume', async () => {
        const returnedFromService = { id: 123, receivedAt: dayjs(currentDate).format(DATE_TIME_FORMAT), ...elemDefault };
        const expected = { receivedAt: currentDate, ...returnedFromService };

        axiosStub.post.mockResolvedValue({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a CandidateResume', async () => {
        axiosStub.post.mockRejectedValue(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a CandidateResume', async () => {
        const returnedFromService = {
          versionNumber: 1,
          receivedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          originalFileName: 'BBBBBB',
          mediaType: 'BBBBBB',
          fileSize: 1,
          sha256: 'BBBBBB',
          storageKey: 'BBBBBB',
          structuredSummary: 'BBBBBB',
          activeVersion: true,
          uploadedBy: 'BBBBBB',
          ...elemDefault,
        };

        const expected = { receivedAt: currentDate, ...returnedFromService };
        axiosStub.put.mockResolvedValue({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a CandidateResume', async () => {
        axiosStub.put.mockRejectedValue(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a CandidateResume', async () => {
        const patchObject = {
          versionNumber: 1,
          originalFileName: 'BBBBBB',
          mediaType: 'BBBBBB',
          fileSize: 1,
          storageKey: 'BBBBBB',
          structuredSummary: 'BBBBBB',
          activeVersion: true,
          uploadedBy: 'BBBBBB',
          ...new CandidateResume(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { receivedAt: currentDate, ...returnedFromService };
        axiosStub.patch.mockResolvedValue({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a CandidateResume', async () => {
        axiosStub.patch.mockRejectedValue(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of CandidateResume', async () => {
        const returnedFromService = {
          versionNumber: 1,
          receivedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          originalFileName: 'BBBBBB',
          mediaType: 'BBBBBB',
          fileSize: 1,
          sha256: 'BBBBBB',
          storageKey: 'BBBBBB',
          structuredSummary: 'BBBBBB',
          activeVersion: true,
          uploadedBy: 'BBBBBB',
          ...elemDefault,
        };
        const expected = { receivedAt: currentDate, ...returnedFromService };
        axiosStub.get.mockResolvedValue([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of CandidateResume', async () => {
        axiosStub.get.mockRejectedValue(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a CandidateResume', async () => {
        axiosStub.delete.mockResolvedValue({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a CandidateResume', async () => {
        axiosStub.delete.mockRejectedValue(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
