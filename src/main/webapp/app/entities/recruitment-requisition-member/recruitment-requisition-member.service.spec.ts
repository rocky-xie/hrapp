import { beforeEach, describe, expect, it, vi } from 'vitest';

import axios from 'axios';
import dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from '@/shared/composables/date-format';
import { RecruitmentRequisitionMember } from '@/shared/model/recruitment-requisition-member.model';

import RecruitmentRequisitionMemberService from './recruitment-requisition-member.service';

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
  describe('RecruitmentRequisitionMember Service', () => {
    let service: RecruitmentRequisitionMemberService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new RecruitmentRequisitionMemberService();
      currentDate = new Date();
      elemDefault = new RecruitmentRequisitionMember(123, 'AAAAAAA', 'AAAAAAA', false, currentDate, currentDate);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = {
          createdAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          removedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
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

      it('should create a RecruitmentRequisitionMember', async () => {
        const returnedFromService = {
          id: 123,
          createdAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          removedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...elemDefault,
        };
        const expected = { createdAt: currentDate, removedAt: currentDate, ...returnedFromService };

        axiosStub.post.mockResolvedValue({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a RecruitmentRequisitionMember', async () => {
        axiosStub.post.mockRejectedValue(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a RecruitmentRequisitionMember', async () => {
        const returnedFromService = {
          username: 'BBBBBB',
          memberType: 'BBBBBB',
          active: true,
          createdAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          removedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...elemDefault,
        };

        const expected = { createdAt: currentDate, removedAt: currentDate, ...returnedFromService };
        axiosStub.put.mockResolvedValue({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a RecruitmentRequisitionMember', async () => {
        axiosStub.put.mockRejectedValue(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a RecruitmentRequisitionMember', async () => {
        const patchObject = {
          username: 'BBBBBB',
          removedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...new RecruitmentRequisitionMember(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { createdAt: currentDate, removedAt: currentDate, ...returnedFromService };
        axiosStub.patch.mockResolvedValue({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a RecruitmentRequisitionMember', async () => {
        axiosStub.patch.mockRejectedValue(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of RecruitmentRequisitionMember', async () => {
        const returnedFromService = {
          username: 'BBBBBB',
          memberType: 'BBBBBB',
          active: true,
          createdAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          removedAt: dayjs(currentDate).format(DATE_TIME_FORMAT),
          ...elemDefault,
        };
        const expected = { createdAt: currentDate, removedAt: currentDate, ...returnedFromService };
        axiosStub.get.mockResolvedValue([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of RecruitmentRequisitionMember', async () => {
        axiosStub.get.mockRejectedValue(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a RecruitmentRequisitionMember', async () => {
        axiosStub.delete.mockResolvedValue({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a RecruitmentRequisitionMember', async () => {
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
