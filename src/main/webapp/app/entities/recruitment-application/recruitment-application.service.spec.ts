import { beforeEach, describe, expect, it, vi } from 'vitest';

import axios from 'axios';
import dayjs from 'dayjs';

import { DATE_FORMAT, DATE_TIME_FORMAT } from '@/shared/composables/date-format';
import { RecruitmentApplication } from '@/shared/model/recruitment-application.model';

import RecruitmentApplicationService from './recruitment-application.service';

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
  describe('RecruitmentApplication Service', () => {
    let service: RecruitmentApplicationService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new RecruitmentApplicationService();
      currentDate = new Date();
      elemDefault = new RecruitmentApplication(
        123,
        'AAAAAAA',
        'GRADUATE',
        'RESUME_SCREENING',
        'IN_PROGRESS',
        'HIRED',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        currentDate,
        currentDate,
        currentDate,
        0,
        currentDate,
        'AAAAAAA',
        'AAAAAAA',
      );
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = {
          appliedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          closedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          retentionUntil: dayjs(currentDate).format(DATE_FORMAT),
          convertedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...elemDefault,
        };
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

      it('should create a RecruitmentApplication', async () => {
        const returnedFromService = {
          id: 123,
          appliedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          closedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          retentionUntil: dayjs(currentDate).format(DATE_FORMAT),
          convertedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...elemDefault,
        };
        const expected = {
          appliedAt: currentDate,
          closedAt: currentDate,
          retentionUntil: currentDate,
          convertedAt: currentDate,
          ...returnedFromService,
        };

        axiosStub.post.mockResolvedValue({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a RecruitmentApplication', async () => {
        axiosStub.post.mockRejectedValue(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a RecruitmentApplication', async () => {
        const returnedFromService = {
          applicationCode: 'BBBBBB',
          recruitmentType: 'BBBBBB',
          currentStage: 'BBBBBB',
          status: 'BBBBBB',
          decision: 'BBBBBB',
          sourceType: 'BBBBBB',
          sourceDetail: 'BBBBBB',
          batchName: 'BBBBBB',
          appliedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          closedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          retentionUntil: dayjs(currentDate).format(DATE_FORMAT),
          convertedPersonId: 1,
          convertedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          closeReason: 'BBBBBB',
          note: 'BBBBBB',
          ...elemDefault,
        };

        const expected = {
          appliedAt: currentDate,
          closedAt: currentDate,
          retentionUntil: currentDate,
          convertedAt: currentDate,
          ...returnedFromService,
        };
        axiosStub.put.mockResolvedValue({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a RecruitmentApplication', async () => {
        axiosStub.put.mockRejectedValue(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a RecruitmentApplication', async () => {
        const patchObject = {
          applicationCode: 'BBBBBB',
          status: 'BBBBBB',
          decision: 'BBBBBB',
          appliedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          closedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          retentionUntil: dayjs(currentDate).format(DATE_FORMAT),
          closeReason: 'BBBBBB',
          ...new RecruitmentApplication(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = {
          appliedAt: currentDate,
          closedAt: currentDate,
          retentionUntil: currentDate,
          convertedAt: currentDate,
          ...returnedFromService,
        };
        axiosStub.patch.mockResolvedValue({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a RecruitmentApplication', async () => {
        axiosStub.patch.mockRejectedValue(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of RecruitmentApplication', async () => {
        const returnedFromService = {
          applicationCode: 'BBBBBB',
          recruitmentType: 'BBBBBB',
          currentStage: 'BBBBBB',
          status: 'BBBBBB',
          decision: 'BBBBBB',
          sourceType: 'BBBBBB',
          sourceDetail: 'BBBBBB',
          batchName: 'BBBBBB',
          appliedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          closedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          retentionUntil: dayjs(currentDate).format(DATE_FORMAT),
          convertedPersonId: 1,
          convertedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          closeReason: 'BBBBBB',
          note: 'BBBBBB',
          ...elemDefault,
        };
        const expected = {
          appliedAt: currentDate,
          closedAt: currentDate,
          retentionUntil: currentDate,
          convertedAt: currentDate,
          ...returnedFromService,
        };
        axiosStub.get.mockResolvedValue([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of RecruitmentApplication', async () => {
        axiosStub.get.mockRejectedValue(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a RecruitmentApplication', async () => {
        axiosStub.delete.mockResolvedValue({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a RecruitmentApplication', async () => {
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
