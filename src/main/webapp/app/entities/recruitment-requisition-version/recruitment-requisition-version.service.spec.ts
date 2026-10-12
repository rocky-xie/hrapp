import { beforeEach, describe, expect, it, vi } from 'vitest';

import axios from 'axios';
import dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from '@/shared/composables/date-format';
import { RecruitmentRequisitionVersion } from '@/shared/model/recruitment-requisition-version.model';

import RecruitmentRequisitionVersionService from './recruitment-requisition-version.service';

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
  describe('RecruitmentRequisitionVersion Service', () => {
    let service: RecruitmentRequisitionVersionService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new RecruitmentRequisitionVersionService();
      currentDate = new Date();
      elemDefault = new RecruitmentRequisitionVersion(123, 0, 'AAAAAAA', currentDate, 'AAAAAAA', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = { lockedAt: dayjs(currentDate).format(DATE_TIME_FORMAT), ...elemDefault };
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

      it('should create a RecruitmentRequisitionVersion', async () => {
        const returnedFromService = { id: 123, lockedAt: dayjs(currentDate).format(DATE_TIME_FORMAT), ...elemDefault };
        const expected = { lockedAt: currentDate, ...returnedFromService };

        axiosStub.post.mockResolvedValue({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a RecruitmentRequisitionVersion', async () => {
        axiosStub.post.mockRejectedValue(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a RecruitmentRequisitionVersion', async () => {
        const returnedFromService = {
          versionNumber: 1,
          snapshotJson: 'BBBBBB',
          lockedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          lockedBy: 'BBBBBB',
          changeReason: 'BBBBBB',
          ...elemDefault,
        };

        const expected = { lockedAt: currentDate, ...returnedFromService };
        axiosStub.put.mockResolvedValue({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a RecruitmentRequisitionVersion', async () => {
        axiosStub.put.mockRejectedValue(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a RecruitmentRequisitionVersion', async () => {
        const patchObject = { lockedBy: 'BBBBBB', changeReason: 'BBBBBB', ...new RecruitmentRequisitionVersion() };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { lockedAt: currentDate, ...returnedFromService };
        axiosStub.patch.mockResolvedValue({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a RecruitmentRequisitionVersion', async () => {
        axiosStub.patch.mockRejectedValue(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of RecruitmentRequisitionVersion', async () => {
        const returnedFromService = {
          versionNumber: 1,
          snapshotJson: 'BBBBBB',
          lockedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          lockedBy: 'BBBBBB',
          changeReason: 'BBBBBB',
          ...elemDefault,
        };
        const expected = { lockedAt: currentDate, ...returnedFromService };
        axiosStub.get.mockResolvedValue([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of RecruitmentRequisitionVersion', async () => {
        axiosStub.get.mockRejectedValue(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a RecruitmentRequisitionVersion', async () => {
        axiosStub.delete.mockResolvedValue({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a RecruitmentRequisitionVersion', async () => {
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
